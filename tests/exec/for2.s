	.text
	.globl main
main:
	subq $8, %rsp
	movq $10, %rdi
	movq %rdi, 0(%rsp)
L13:
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	jg L11
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
L11:
	movq 0(%rsp), %rdi
	subq $1, %rdi
	movq %rdi, 0(%rsp)
	addq $1, %rdi
	addq $65, %rdi
	call putchar
	jmp L13
	.data
