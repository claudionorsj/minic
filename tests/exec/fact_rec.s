	.text
	.globl main
fact_rec:
	subq $8, %rsp
	cmpq $1, %rdi
	jle L7
	movq %rdi, 0(%rsp)
	subq $1, %rdi
	call fact_rec
	movq 0(%rsp), %r11
	imulq %rax, %r11
	movq %r11, 0(%rsp)
L1:
	movq 0(%rsp), %rax
	addq $8, %rsp
	ret
L7:
	movq $1, 0(%rsp)
	jmp L1
main:
	movq $0, %rdi
	call fact_rec
	subq $1, %rax
	cmpq $0, %rax
	je L27
L25:
	movq $1, %rdi
	call fact_rec
	subq $1, %rax
	cmpq $0, %rax
	je L21
L19:
	movq $5, %rdi
	call fact_rec
	subq $120, %rax
	cmpq $0, %rax
	je L15
L13:
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L15:
	movq $51, %rdi
	call putchar
	jmp L13
L21:
	movq $50, %rdi
	call putchar
	jmp L19
L27:
	movq $49, %rdi
	call putchar
	jmp L25
	.data
