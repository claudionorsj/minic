	.text
	.globl main
make:
	subq $8, %rsp
	movq %rdi, 0(%rsp)
	movq $24, %rdi
	call sbrk
	movq 0(%rsp), %rdi
	movq %rdi, 0(%rax)
	movq %rax, %rdi
	movq %rax, 16(%rdi)
	movq %rax, %rdi
	movq %rax, 8(%rdi)
	addq $8, %rsp
	ret
inserer_apres:
	subq $8, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, %rdi
	call make
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rax)
	movq 0(%rsp), %rdi
	movq %rax, 8(%rdi)
	movq %rax, %rdi
	movq 8(%rdi), %rdi
	movq %rax, 16(%rdi)
	movq 0(%rsp), %rdi
	movq %rdi, 16(%rax)
	movq $0, %rax
	addq $8, %rsp
	ret
supprimer:
	movq 8(%rdi), %rsi
	movq 16(%rdi), %rcx
	movq %rsi, 8(%rcx)
	movq 16(%rdi), %rcx
	movq 8(%rdi), %rdi
	movq %rcx, 16(%rdi)
	movq $0, %rax
	ret
afficher:
	subq $16, %rsp
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	movq %rdi, 8(%rsp)
	movq 8(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 8(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rsp)
L59:
	movq 8(%rsp), %rcx
	movq 0(%rsp), %rdi
	subq %rdi, %rcx
	cmpq $0, %rcx
	je L48
	movq 8(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 8(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rsp)
	jmp L59
L48:
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
cercle:
	subq $24, %rsp
	movq %rdi, 0(%rsp)
	movq $1, %rdi
	call make
	movq %rax, 16(%rsp)
	movq 0(%rsp), %rdi
	movq %rdi, 8(%rsp)
L78:
	movq 8(%rsp), %rdi
	cmpq $2, %rdi
	jge L76
	movq 16(%rsp), %rax
	addq $24, %rsp
	ret
L76:
	movq 16(%rsp), %rdi
	movq 8(%rsp), %rsi
	call inserer_apres
	movq 8(%rsp), %rdi
	subq $1, %rdi
	movq %rdi, 8(%rsp)
	jmp L78
josephus:
	subq $16, %rsp
	movq %rsi, 0(%rsp)
	call cercle
	movq %rax, 8(%rsp)
L109:
	movq 8(%rsp), %rcx
	movq 8(%rsp), %rdi
	movq 8(%rdi), %rdi
	subq %rdi, %rcx
	cmpq $0, %rcx
	je L86
	movq $1, %rdi
L102:
	movq 0(%rsp), %rcx
	cmpq %rcx, %rdi
	jl L99
	movq 8(%rsp), %rdi
	call supprimer
	movq 8(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rsp)
	jmp L109
L99:
	movq 8(%rsp), %rcx
	movq 8(%rcx), %rcx
	movq %rcx, 8(%rsp)
	addq $1, %rdi
	jmp L102
L86:
	movq 8(%rsp), %rdi
	movq 0(%rdi), %rax
	addq $16, %rsp
	ret
print_int:
	subq $16, %rsp
	movq %rbx, 0(%rsp)
	movq %rdi, 8(%rsp)
	movq 8(%rsp), %rax
	movq $10, %rdi
	movq %rdx, %rbx
	movq $0, %rdx
	idivq %rdi
	movq %rax, %rbx
	movq 8(%rsp), %rdi
	cmpq $9, %rdi
	jg L123
L121:
	movq 8(%rsp), %rdi
	movq $10, %rcx
	imulq %rbx, %rcx
	subq %rcx, %rdi
	addq $48, %rdi
	call putchar
	movq $0, %rax
	movq 0(%rsp), %rbx
	addq $16, %rsp
	ret
L123:
	movq %rbx, %rdi
	call print_int
	jmp L121
main:
	movq $7, %rdi
	movq $5, %rsi
	call josephus
	movq %rax, %rdi
	call print_int
	movq $10, %rdi
	call putchar
	movq $5, %rdi
	movq $5, %rsi
	call josephus
	movq %rax, %rdi
	call print_int
	movq $10, %rdi
	call putchar
	movq $5, %rdi
	movq $17, %rsi
	call josephus
	movq %rax, %rdi
	call print_int
	movq $10, %rdi
	call putchar
	movq $13, %rdi
	movq $2, %rsi
	call josephus
	movq %rax, %rdi
	call print_int
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
