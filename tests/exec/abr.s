	.text
	.globl main
make:
	subq $24, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, 8(%rsp)
	movq %rdx, 16(%rsp)
	movq $24, %rdi
	call sbrk
	movq 0(%rsp), %rdi
	movq %rdi, 0(%rax)
	movq 8(%rsp), %rdi
	movq %rdi, 8(%rax)
	movq 16(%rsp), %rdi
	movq %rdi, 16(%rax)
	addq $24, %rsp
	ret
insere:
	subq $8, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, %rcx
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	subq %rdi, %rcx
	cmpq $0, %rcx
	je L49
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	cmpq %rdi, %rsi
	jl L30
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	cmpq $0, %rdi
	je L36
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	call insere
L16:
	movq $0, %rax
L15:
	addq $8, %rsp
	ret
L36:
	movq %rsi, %rdi
	movq $0, %rsi
	movq $0, %rdx
	call make
	movq 0(%rsp), %rdi
	movq %rax, 16(%rdi)
	jmp L16
L30:
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	cmpq $0, %rdi
	je L22
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	call insere
	jmp L16
L22:
	movq %rsi, %rdi
	movq $0, %rsi
	movq $0, %rdx
	call make
	movq 0(%rsp), %rdi
	movq %rax, 8(%rdi)
	jmp L16
L49:
	movq $0, %rax
	jmp L15
contient:
	movq %rsi, %rcx
	movq 0(%rdi), %rdx
	subq %rdx, %rcx
	cmpq $0, %rcx
	je L80
	movq 0(%rdi), %rcx
	cmpq $0, %rcx
	je L71
	movq 8(%rdi), %rcx
	cmpq $0, %rcx
	jne L70
L71:
	movq $0, %rcx
L69:
	cmpq %rcx, %rsi
	jl L68
	movq 16(%rdi), %rcx
	cmpq $0, %rcx
	jne L60
	movq $0, %rax
L55:
	ret
L60:
	movq 16(%rdi), %rdi
	call contient
	jmp L55
L68:
	movq 8(%rdi), %rdi
	call contient
	jmp L55
L70:
	movq $1, %rcx
	jmp L69
L80:
	movq $1, %rax
	jmp L55
print_int:
	subq $16, %rsp
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rax
	movq $10, %rdi
	movq $0, %rdx
	idivq %rdi
	movq %rax, 8(%rsp)
	movq 0(%rsp), %rdi
	cmpq $9, %rdi
	jg L96
L94:
	movq 0(%rsp), %rdi
	movq $10, %rsi
	movq 8(%rsp), %rcx
	imulq %rcx, %rsi
	subq %rsi, %rdi
	addq $48, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
L96:
	movq 8(%rsp), %rdi
	call print_int
	jmp L94
print:
	subq $8, %rsp
	movq %rdi, 0(%rsp)
	movq $40, %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	cmpq $0, %rdi
	jne L118
L115:
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	call print_int
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	cmpq $0, %rdi
	jne L108
L105:
	movq $41, %rdi
	call putchar
	addq $8, %rsp
	ret
L108:
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	call print
	jmp L105
L118:
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	call print
	jmp L115
main:
	subq $8, %rsp
	movq $1, %rdi
	movq $0, %rsi
	movq $0, %rdx
	call make
	movq %rax, 0(%rsp)
	movq 0(%rsp), %rdi
	movq $17, %rsi
	call insere
	movq 0(%rsp), %rdi
	movq $5, %rsi
	call insere
	movq 0(%rsp), %rdi
	movq $8, %rsi
	call insere
	movq 0(%rsp), %rdi
	call print
	movq $10, %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq $5, %rsi
	call contient
	cmpq $0, %rax
	je L139
	movq 0(%rsp), %rdi
	movq $0, %rsi
	call contient
	cmpq $0, %rax
	je L158
	movq $0, %rdi
L157:
	cmpq $0, %rdi
	je L139
	movq 0(%rsp), %rdi
	movq $17, %rsi
	call contient
	cmpq $0, %rax
	je L139
	movq 0(%rsp), %rdi
	movq $3, %rsi
	call contient
	cmpq $0, %rax
	je L147
	movq $0, %rdi
L146:
	cmpq $0, %rdi
	je L139
	movq $111, %rdi
	call putchar
	movq $107, %rdi
	call putchar
	movq $10, %rdi
	call putchar
L139:
	movq 0(%rsp), %rdi
	movq $42, %rsi
	call insere
	movq 0(%rsp), %rdi
	movq $1000, %rsi
	call insere
	movq 0(%rsp), %rdi
	movq $0, %rsi
	call insere
	movq 0(%rsp), %rdi
	call print
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
L147:
	movq $1, %rdi
	jmp L146
L158:
	movq $1, %rdi
	jmp L157
	.data
